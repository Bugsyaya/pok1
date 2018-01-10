package database


import model.{Entreprise, Module, Personne, Stagiaire}
import play.api.libs.json._
import reactivemongo.api._
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.NonFatal
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt


case class MongoConf(
	                    host: String,
	                    port: Int,
	                    dbName: String
                    )

case class MongoDB(conf: MongoConf) extends DBDriver {
	
	val uri = s"""mongodb://${conf.host}:${conf.port}/${conf.dbName}"""
	
	lazy val mongoConn = Future.fromTry(MongoConnection.parseURI(uri).map(MongoDriver().connection))
	
	lazy val defaultDB: Future[DefaultDB] =
		(for {
			con <- mongoConn
			db <- con.database(conf.dbName)
		} yield db)
			.recoverWith { case NonFatal(ex) => Future.failed(new RuntimeException(s"MongoDb not reachable $uri", ex)) }
	
	override def close(): Future[Unit] = {
		println(s"############ Closing DB... ${conf.port} #####################")
		mongoConn
			.flatMap(_.askClose()(60.seconds))
			.map(_ => println(s"############ Closing DB!!! ${conf.port} #####################"))
	}
	
	val StagiaireCollection = new StagiaireCollection {
		private def collectionStagiaire: Future[JSONCollection] =
			defaultDB
				.map(_.collection[JSONCollection]("stagiaire"))
				.recover {
					case e: Exception =>
						throw new RuntimeException("db not reachable")
				}
		
		override def create(stagiaire: Stagiaire): Future[WriteResult] =
			for {
				collection <- collectionStagiaire
				created <- collection.insert[Stagiaire](stagiaire)
			} yield created
		
		override def all: Future[Seq[Stagiaire]] =
			for {
				collection <- collectionStagiaire
				result <- collection
					.find[JsObject](JsObject(Seq.empty[(String, JsValue)]))
					.cursor[Stagiaire]()
					.collect[Seq]()
			} yield result
		
		override def byId(stagiaireId: String): Future[Option[Stagiaire]] =
			for {
				collection <- collectionStagiaire
				result <- collection.find[JsObject](JsObject(Seq("infoStagiaire.id" -> JsString(stagiaireId)))).one[Stagiaire]
			} yield result
		
	}
	
	val EntrepriseCollection = new EntrepriseCollection {
		private def collectionEntreprise: Future[JSONCollection] =
			defaultDB
				.map(_.collection[JSONCollection]("entreprise"))
				.recover {
					case e: Exception =>
						throw new RuntimeException("db not reachable")
				}
		
		override def create(entreprise: Entreprise): Future[WriteResult] =
			for {
				collection <- collectionEntreprise
				created <- collection.insert[Entreprise](entreprise)
			} yield created
		
		override def all: Future[Seq[Entreprise]] =
			for {
				collection <- collectionEntreprise
				result <- collection
					.find[JsObject](JsObject(Seq.empty[(String, JsValue)]))
					.cursor[Entreprise]()
					.collect[Seq]()
			} yield result
		
		override def byId(entrepriseId: String): Future[Option[Entreprise]] =
			for {
				collection <- collectionEntreprise
				result <- collection.find[JsObject](JsObject(Seq("id" -> JsString(entrepriseId)))).one[Entreprise]
			} yield result
		
	}
	
	val ModuleCollection = new ModuleCollection {
		private def collectionModule: Future[JSONCollection] =
			defaultDB
				.map(_.collection[JSONCollection]("module"))
				.recover {
					case e: Exception =>
						throw new RuntimeException("db not reachable")
				}
		
		override def all: Future[Seq[Module]] =
			for {
				collection <- collectionModule
				result <- collection
					.find[JsObject](JsObject(Seq.empty[(String, JsValue)]))
					.cursor[Module]()
					.collect[Seq]()
			} yield result
		
		override def byFormationId(formationId: String): Future[Option[Module]] =
			for {
				collection <- collectionModule
				result <- collection.find[JsObject](JsObject(Seq("formationId" -> JsString(formationId)))).one[Module]
			} yield result
		
	}
}