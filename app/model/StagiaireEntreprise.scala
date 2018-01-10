package model

import play.api.libs.json.Reads._
import play.api.libs.json._
import java.util.UUID._


object StagiaireEntreprise {
	implicit val format = Json.format[StagiaireEntreprise]
}


case class StagiaireEntreprise(
	                    stagiaire: Stagiaire,
	                    entreprise: Option[Entreprise]
                    )