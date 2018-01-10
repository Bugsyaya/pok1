package model

import play.api.libs.json.Json

object SousModule {
	implicit val format = Json.format[SousModule]
}


case class SousModule(
	                 nomModule: String,
	                 moduleId: String,
	                 nbSemaine: Int,
	                 depend: Seq[String]
                 )