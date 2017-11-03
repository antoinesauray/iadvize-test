package com.iadvize.vdm

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, NativeSwaggerBase, Swagger}

 /**
  * Created by Antoine Sauray on 02/11/2017.
   * Swagger
  */
class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with NativeSwaggerBase

object IAdvizeApiInfo extends ApiInfo(
  "iAdvize Backend Test",
  "Docs for iAdvize backend test",
  "http://localhost:8080/swagger",
  "antoine.sauray@etu.univ-nantes.fr",
  "Apache2",
  "https://www.apache.org/licenses/LICENSE-2.0")

class VDMSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", IAdvizeApiInfo)