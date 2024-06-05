package com.konai.vam.api.config

import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DataSourceConnectionTest {
    @Autowired
    private lateinit var dataSource: DataSource

    // logger
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Test
    fun `HikariCP max pool size 초과 에러 확인한다`() {
        val hikariDataSource = dataSource as HikariDataSource

        val maxPoolSize = hikariDataSource.maximumPoolSize
        val connections = mutableListOf<Connection>()

        try {
            // 최대 풀 크기까지 연결을 생성
            repeat(maxPoolSize) {
                connections.add(hikariDataSource.connection)
            }

            // 추가 연결 시도
            assertThrows<SQLException> { hikariDataSource.connection }
        } finally {
            // 모든 연결을 종료
            connections.forEach { it.close() }
        }
    }

    @Test
    fun `HikariCP max-life-time 설정하여 설정 시간에 모든 idle-connection 제거 확인한다`() {
        assertTrue(dataSource is HikariDataSource)
        val hikariDataSource = dataSource as HikariDataSource
        /**
         * idleTimeout > maxLifetime : `idleTimeout` 만큼 idle 유지
         */
        hikariDataSource.idleTimeout = 10000
        hikariDataSource.maxLifetime = 5000

        val maxPoolSize = hikariDataSource.maximumPoolSize
        val minimumIdle = hikariDataSource.minimumIdle
        val idleTimeout = hikariDataSource.idleTimeout
        val maxLifetime = hikariDataSource.maxLifetime
        val connections = mutableListOf<Connection>()

        // Step 1: max-pool-size 만큼 connection 생성
        repeat(maxPoolSize) {
            connections.add(hikariDataSource.connection)
        }

        // Step 2: 모든 connection idle 상태로 변경
        connections.forEach { it.close() }
        connections.clear()

        // step 3: connection 1개 추가 생성
        connections.add(hikariDataSource.connection)

        logger.info("Waiting for max-life-time to exceed: ${maxLifetime + 1000} ms")
        Thread.sleep(maxLifetime + 1000)

        // Step 4: idle-timeout 경과 후 idle/active 상태 connection 갯수 확인
        val idleConnectionsAfterTimeout = hikariDataSource.hikariPoolMXBean.idleConnections
        logger.info("Idle connections after idle timeout: $idleConnectionsAfterTimeout")
        val activeConnectionsAfterTimeout = hikariDataSource.hikariPoolMXBean.activeConnections
        logger.info("Active connections after idle timeout: $activeConnectionsAfterTimeout")

        assertThat(idleConnectionsAfterTimeout).isEqualTo(minimumIdle)
        assertThat(activeConnectionsAfterTimeout).isEqualTo(connections.size)

        connections.forEach { it.close() }
        connections.clear()
    }

}