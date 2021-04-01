package com.cisco.ccc.cats

import com.amazonaws.services.kms.AWSKMSClientBuilder
import com.amazonaws.services.kms.AWSKMS
import com.amazonaws.services.kms.model.GenerateDataKeyRequest

object KeyBroker {
  def main(args: Array[String]) : Unit = {
    val client : AWSKMS = AWSKMSClientBuilder.standard().build()
    val request = new GenerateDataKeyRequest().withKeyId("alias/taskId1").withKeySpec("AES_256")
    val response = client.generateDataKey(request)
    println(response)
  }
}