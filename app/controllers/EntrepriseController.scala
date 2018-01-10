package controllers

import java.util.UUID.randomUUID
import javax.inject._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import database.{MongoConf, MongoDB}
import model.{Adresse, Entreprise, Personne}
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class EntrepriseController @Inject()(cc : ControllerComponents) extends AbstractController(cc){
	val mongodb = MongoConf("localhost", 27017, "databasePok1")
	val db = MongoDB(mongodb)
	
	implicit val system: ActorSystem = ActorSystem()
	implicit val ec: ExecutionContext = system.dispatcher
	implicit val mat: ActorMaterializer = ActorMaterializer()
	
	
	def create = Action { request =>
		val entreprise: Entreprise = Json.fromJson[Entreprise](request.body.asJson.get).get
		println(s"entrprise : ${entreprise}")
		val completEntreprise = Entreprise(
			Some(randomUUID().toString),
			entreprise.nomEntreprise,
			entreprise.raisonSocial,
			Personne(
				nom = entreprise.contact.nom,
				prenom = entreprise.contact.prenom,
				telFix = entreprise.contact.telFix,
				adresse = Adresse(
					entreprise.contact.adresse.numero,
					entreprise.contact.adresse.rue,
					entreprise.contact.adresse.ville,
					entreprise.contact.adresse.codePostal
				)
			),
			Adresse(
				entreprise.adresse.numero,
				entreprise.adresse.rue,
				entreprise.adresse.ville,
				entreprise.adresse.codePostal
			)
		)
		db.EntrepriseCollection.create(completEntreprise)
		Ok(toJson[Entreprise](completEntreprise))
	}
	
	def show = Action.async { request =>
		db.EntrepriseCollection.all.map{ent =>
			Ok(toJson[Seq[Entreprise]](ent))
		}
	}
	
	def details(id: String) = Action.async { request =>
		db.EntrepriseCollection.byId(id).map{optEntreprise =>
			println(s"id : $id")
			println(s"optStagiaire : $optEntreprise")
			Ok(toJson[Option[Entreprise]](optEntreprise))
		}
	}
}
