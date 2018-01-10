package model

import play.api.libs.json.Json

object Adresse {
	implicit val format = Json.format[Adresse]
}

case class Adresse (
	numero: String,
	rue: String,
	ville: String,
	codePostal: String
	)