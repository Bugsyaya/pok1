package database

import model.{Entreprise, Module, Personne, Stagiaire}
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait StagiaireCollection{
	def create(stagiaire: Stagiaire): Future[WriteResult]
	
	def all: Future[Seq[Stagiaire]]
	
	def byId(stagiaireId: String): Future[Option[Stagiaire]]
}

trait EntrepriseCollection{
	def create(entreprise: Entreprise): Future[WriteResult]
	
	def all: Future[Seq[Entreprise]]
	
	def byId(entrepriseId: String): Future[Option[Entreprise]]
}

trait ModuleCollection{
	def all: Future[Seq[Module]]
	
	def byFormationId(formationId: String): Future[Option[Module]]
}

trait DBDriver {
	val StagiaireCollection: StagiaireCollection
	
	def close(): Future[Unit]
}
