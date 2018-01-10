package model

import play.api.libs.json.Reads._
import play.api.libs.json._
import java.util.UUID._


object Personne {
	implicit val format = Json.format[Personne]
}


case class Personne(
	                     id: Option[String] = Some(randomUUID().toString),
	                     nom: String,
	                     prenom: String,
	                     telFix: Option[String] = None,
	                     adresse: Adresse
                     )