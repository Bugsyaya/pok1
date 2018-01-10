package controllers

import javax.inject._

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import database.{MongoConf, MongoDB}
import model._
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class ModulesController @Inject()(cc : ControllerComponents) extends AbstractController(cc){
	val mongodb = MongoConf("localhost", 27017, "databasePok1")
	val db = MongoDB(mongodb)
	
	implicit val system: ActorSystem = ActorSystem()
	implicit val ec: ExecutionContext = system.dispatcher
	implicit val mat: ActorMaterializer = ActorMaterializer()
	
	def show = Action.async { request =>
		db.ModuleCollection.all.map { mod =>
			Ok(toJson[Seq[Module]](mod))
		}
	}
	
	def details(id: String) = Action.async { request =>
		db.ModuleCollection.byFormationId(id).map{optModule =>
			Ok(toJson[Option[Module]](optModule))
		}
	}
	
//	def isCorrect(modules: Seq[(Int, Module)]) = Action{ request =>
//		Ok("")
//	}
	
//	private def isC(modules: Seq[(Int, SousModule)]) = {
//		def isCorrectRect(modules: Seq[(Int, SousModule)], acc: Seq[(Boolean, SousModule)]) = {
//			if (modules.isEmpty) acc
//			else {
//				modules.map{module: (Int, SousModule) =>
//					modules.filter(module2 => module._1 < module2._1 && module._2.depend.nonEmpty)
//					    	.map{module2: (Int, SousModule) =>
//							    modu
//						    }
//				}
//			}
//		}
//		isCorrectRect(modules, Seq.empty[(Boolean, Module)])
//	}
}
