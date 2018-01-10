package model

import play.api.libs.json.Reads._
import play.api.libs.json._
import java.util.UUID._


object Stagiaire {
	implicit val format = Json.format[Stagiaire]
}


case class Stagiaire(
	                   infoStagiaire: Personne,
	                   idEntreprise: Option[String] = None,
	                   idFormation: String,
	                   planningCourrent: Option[String] = None,
//	                   plannings: Seq[String] = Seq.empty[String]
                   )