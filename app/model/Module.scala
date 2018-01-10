package model

import play.api.libs.json.Reads._
import play.api.libs.json._
import java.util.UUID._


object Module {
	implicit val format = Json.format[Module]
}


case class Module(
	                  id: Option[String] = Some(randomUUID().toString),
	                  formationId: String,
	                  modules: Seq[SousModule]
                  )