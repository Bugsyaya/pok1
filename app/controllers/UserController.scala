package controllers

import java.util.UUID.randomUUID

import database.{MongoConf, MongoDB}
import play.api.mvc._
import javax.inject._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.json.Json.{obj, toJson}
import play.api.libs.json._
import model._

import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject() (cc : ControllerComponents) extends AbstractController(cc){
	val mongodb = MongoConf("localhost", 27017, "databasePok1")
	val db = MongoDB(mongodb)
	
	implicit val system: ActorSystem = ActorSystem()
	implicit val ec: ExecutionContext = system.dispatcher
	implicit val mat: ActorMaterializer = ActorMaterializer()
	
	
	def create = Action.async { request =>
		val stagi: Stagiaire = Json.fromJson[Stagiaire](request.body.asJson.get).get
		val completStagiaire = Stagiaire(
			Personne(
				nom = stagi.infoStagiaire.nom,
				prenom = stagi.infoStagiaire.prenom,
				telFix = stagi.infoStagiaire.telFix,
				adresse = Adresse(
					stagi.infoStagiaire.adresse.numero,
					stagi.infoStagiaire.adresse.rue,
					stagi.infoStagiaire.adresse.ville,
					stagi.infoStagiaire.adresse.codePostal
				)
			),
			stagi.idEntreprise,
			stagi.idFormation,
			stagi.planningCourrent
		)
		db.StagiaireCollection.create(completStagiaire).map(wr => Ok({toJson[Stagiaire](completStagiaire)}))
	}
	
	def show = Action.async { request =>
		db.StagiaireCollection.all.flatMap { sts =>
			Future.sequence(
				sts.filter(st => st.idEntreprise.isDefined)
				.map { (st: Stagiaire) =>
					db.EntrepriseCollection.byId(st.idEntreprise.get)
						.map{entreprise =>
							StagiaireEntreprise(st, entreprise)
						}
				}
			)
		}.map{result => Ok(toJson[Seq[StagiaireEntreprise]](result))}
	}
	
	def details(id: String) = Action.async { request =>
		db.StagiaireCollection.byId(id).map{optStagiaire =>
			Ok(toJson[Option[Stagiaire]](optStagiaire))
		}
	}
}
