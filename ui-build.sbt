import java.net.InetSocketAddress

import play.sbt.PlayRunHook

import scala.sys.process.Process

val command_dependencyInstall = "npm install"
val command_test = "npm run test"
val command_serve = "npm run start"
val command_build = "npm run build"

def apply(base: File): PlayRunHook = {
  object UIBuildHook extends PlayRunHook {

    var process: Option[Process] = None

    /**
      * Change these commands if you want to use Yarn.
      */
    var npmInstall: String = command_dependencyInstall
    var npmRun: String = command_serve
    var npmBuild: String = command_build

    // Windows requires npm commands prefixed with cmd /c
    if (System.getProperty("os.name").toLowerCase().contains("win")){
      npmInstall = "cmd /c " + npmInstall
      npmRun = "cmd /c " + npmRun
      npmBuild = "cmd /c " + npmBuild
    }

    /**
      * Executed before play run start.
      * Run npm install if node modules are not installed.
      */
    override def beforeStarted(): Unit = {
      if (!(base / "ui" / "onto-react" / "node_modules").exists()) Process(npmInstall, base / "ui" / "onto-react").!
      Process(npmBuild, base / "ui" / "onto-react").!
    }

    /**
      * Executed after play run start.
      * Run npm start
      */
    override def afterStarted(addr: InetSocketAddress): Unit = {
      process = Option(
        Process(npmRun, base / "ui" / "onto-react").run
      )
    }

    /**
      * Executed after play run stop.
      * Cleanup frontend execution processes.
      */
    override def afterStopped(): Unit = {
      process.foreach(_.destroy())
      process = None
    }

  }

  UIBuildHook
}

/*
 * UI Build hook Scripts
 */

// Execution status success.
val Success = 0

// Execution status failure.
val Error = 1

// Run serve task when Play runs in dev mode, that is, when using 'sbt run'
// https://www.playframework.com/documentation/2.6.x/SBTCookbook
PlayKeys.playRunHooks += baseDirectory.map(apply).value

// True if build running operating system is windows.
val isWindows = System.getProperty("os.name").toLowerCase().contains("win")

// Execute on commandline, depending on the operating system. Used to execute npm commands.
def runOnCommandline(script: String)(implicit dir: File): Int = {
  if(isWindows){ Process("cmd /c set CI=true&&" + script, dir) } else { Process("env CI=true " + script, dir) } }!

// Check of node_modules directory exist in given directory.
def isNodeModulesInstalled(implicit dir: File): Boolean = (dir / "node_modules").exists()

// Execute `npm install` command to install all node module dependencies. Return Success if already installed.
def runNpmInstall(implicit dir: File): Int =
  if (isNodeModulesInstalled) Success else runOnCommandline(command_dependencyInstall)

// Execute task if node modules are installed, else return Error status.
def ifNodeModulesInstalled(task: => Int)(implicit dir: File): Int =
  if (runNpmInstall == Success) task
  else Error

// Execute frontend test task. Update to change the frontend test task.
def executeUiTests(implicit dir: File): Int = ifNodeModulesInstalled(runOnCommandline(command_test))

// Execute frontend prod build task. Update to change the frontend prod build task.
def executeProdBuild(implicit dir: File): Int = ifNodeModulesInstalled(runOnCommandline(command_build))

// Execute frontend start task. Update to change the frontend start task.
def executeUiStart(implicit dir: File): Int = ifNodeModulesInstalled(runOnCommandline(command_serve))

// Create frontend build tasks for prod, dev and test execution.

lazy val `ui-test` = taskKey[Unit]("Run UI tests when testing application.")

`ui-test` := {
  implicit val userInterfaceRoot = baseDirectory.value / "ui" / "onto-react"
  if (executeUiTests != Success) throw new Exception("UI tests failed!")
}

lazy val `ui-prod-build` = taskKey[Unit]("Run UI build when packaging the application.")

`ui-prod-build` := {
  implicit val userInterfaceRoot = baseDirectory.value / "ui" / "onto-react"
  if (executeProdBuild != Success) throw new Exception("Oops! UI Build crashed.")
}

lazy val `ui-start` = taskKey[Unit]("Run UI demo server.")

`ui-start` := {
  implicit val userInterfaceRoot = baseDirectory.value / "ui" / "onto-react"
  if (executeUiStart != Success) throw new Exception("Oops! UI Build crashed.")
}


// Execute frontend prod build task prior to play dist execution.
dist := (dist dependsOn `ui-prod-build`).value

// Execute frontend prod build task prior to play stage execution.
stage := (stage dependsOn `ui-prod-build`).value

// Execute frontend test task prior to play test execution.
test := ((test in Test) dependsOn `ui-test`).value