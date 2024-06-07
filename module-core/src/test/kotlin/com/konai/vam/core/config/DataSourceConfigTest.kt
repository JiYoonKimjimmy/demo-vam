package com.konai.vam.core.config

import com.zaxxer.hikari.HikariDataSource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = [VamCoreApplicationConfig::class], webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DataSourceConfigTest {

    @Autowired
    private lateinit var dataSource: DataSource

    @Test
    fun `HikariCP 최대 Connection 허용 갯수 초과하여 에러 발생한다`() {
        val hikariDataSource = dataSource as HikariDataSource
        val connections = mutableListOf<Connection>()
        val maxPoolSize = hikariDataSource.maximumPoolSize

        try {
            repeat(maxPoolSize) {
                connections.add(hikariDataSource.connection)
            }

            assertThrows<SQLException> { hikariDataSource.connection }

        } finally {
            connections.forEach { it.close() }
        }

        Assertions.assertThat(hikariDataSource.hikariPoolMXBean.activeConnections).isEqualTo(0)
        Assertions.assertThat(hikariDataSource.hikariPoolMXBean.idleConnections).isEqualTo(maxPoolSize)
    }

}