package com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step

import com.konai.vam.batch.v1.virtualaccount.cardmanagement.batch.cardconnect.step.item.VirtualAccountCardConnectItem
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.FormatterLineAggregator
import org.springframework.core.io.FileSystemResource
import java.nio.file.Paths

class VirtualAccountCardConnectItemWriter(
    filePath: String,
    batchId: String,
    quantity: Int,
) : FlatFileItemWriter<VirtualAccountCardConnectItem>() {

    init {
        // File 생성 경로 지정
        setFileResource(filePath, batchId)
        // FormatterLineAggregator 설정
        setLineAggregator(CustomFormatterLineAggregator())
        // Callback 처리
        setHeaderCallback {
            it.write(String.format("BatchId=%s\n", batchId))
            it.write(String.format("Quantity=%06d", quantity))
        }
    }

    private fun setFileResource(outputPath: String, batchId: String) {
        val fileName = "raw_data_additional_$batchId.SEM"
        setResource(FileSystemResource(Paths.get(outputPath, fileName).toString()))
    }

}

class CustomFormatterLineAggregator : FormatterLineAggregator<VirtualAccountCardConnectItem>() {

    init {
        val fieldExtractor = BeanWrapperFieldExtractor<VirtualAccountCardConnectItem>()
        fieldExtractor.setNames(arrayOf("id", "serviceId", "par", "accountNo"))
        setFormat("%06dSVCID=%-15s;par=%-27s;VIRTUALACC=%-16s;")
        setFieldExtractor(fieldExtractor)
    }

}
