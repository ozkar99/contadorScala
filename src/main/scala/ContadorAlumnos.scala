import scala.swing._
import javax.swing.filechooser.FileNameExtensionFilter


//hola
object contadorAlumnos extends App {

  val frm = new FileChooser
  frm.fileFilter = new FileNameExtensionFilter("Archivo Excel", "xlsx")
  frm.showDialog(null, "Seleccione Archivo")

  var filePath = ""

  try
    filePath = frm.selectedFile.getPath.replace("\\", "\\\\")
  catch {
    case npe: NullPointerException => sys.exit // esperado al presionar cancelar.
    case e: Exception => e.printStackTrace //no esperado, imprime exception.
  }

  if (!filePath.endsWith("xlsx")) {
    Dialog.showMessage(null, "Archivo no valido.", "Error", Dialog.Message.Error)
    sys.exit
  }

  val csiExcel = new CSIExcel(filePath)
  csiExcel.process

  Dialog.showMessage(null, "Procesamiento Terminado.", "Fin", Dialog.Message.Info)

}