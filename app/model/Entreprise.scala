package model

import play.api.libs.json.Reads._
import play.api.libs.json._
import java.util.UUID._


object Entreprise {
	implicit val format = Json.format[Entreprise]
}


case class Entreprise (
	                     id: Option[String],
	                     nomEntreprise: String,
	                     raisonSocial: String,
	                     contact: Personne,
	                     adresse: Adresse
                     )