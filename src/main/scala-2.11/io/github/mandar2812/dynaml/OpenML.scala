package io.github.mandar2812.dynaml

import io.github.mandar2812.dynaml.pipes.DataPipe
import org.apache.commons.io.FileUtils
import org.openml.apiconnector.io.OpenmlConnector
import org.openml.apiconnector.xml.DataSetDescription

import scala.io.Source

/**
  * @author mandar date 07/09/16.
  *
  * Object is the one stop for interacting with
  * the OpenML api. It can be used to download and apply
  * DynaML flows on data sets and uplaoding the results
  * back to the OpenML server.
  */
object OpenML {

  val client: OpenmlConnector = new OpenmlConnector()

  private var _cacheLocation = "~/.openml/cache/"

  // Getter
  def cacheLocation = _cacheLocation

  // Setter
  def cacheLocation_= (value:String):Unit = _cacheLocation = value

  /**
    * Establish connection with openml server
    * and log in as per the user API Key provided
    * */
  def connect(apiKey: String): Unit = {
    client.setApiKey(apiKey)
  }

  def clearCache(): Unit = FileUtils.cleanDirectory(new java.io.File(cacheLocation))

  /**
    * Get the attibutes of a particular data set.
    *
    * @param id The numeric id of the data set in the OpenML data collection
    * */
  def dataset(id: Int): DataSetDescription = client.dataGet(id)

  /**
    * Download an OpenML data set as a [[java.io.File]]
    * */
  def downloadDataSet(id: Int): java.io.File = dataset(id).getDataset(client.getApiKey)

  /**
    * Download data set from OpenML and load the
    * text file into a Stream of lines.
    * */
  val openMLDataToStream = DataPipe((id: Int) => {
    Source.fromFile(downloadDataSet(id)).getLines().toStream
  })

}
