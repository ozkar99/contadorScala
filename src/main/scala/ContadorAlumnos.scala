import scala.swing._
import javax.swing.filechooser.FileNameExtensionFilter

object contadorAlumnos extends App {

  val frm = new FileChooser
  frm.fileFilter = new FileNameExtensionFilter("Archivo Excel", "xls", "xlsx")
  frm.showDialog(null, "Seleccione Archivo")

  var filePath = ""

  try {
    filePath = frm.selectedFile.getPath.replace("\\", "\\\\")
  } catch {
      case e: Exception => sys.exit // esperado al presionar cancelar.
  }

  try{
    val csiExcel = new CSIExcel(filePath)
    csiExcel.process
    Dialog.showMessage(null, "Procesamiento Terminado.", "Fin", Dialog.Message.Info)
  }catch {
     case e: Exception => Dialog.showMessage(null, e.getStackTraceString, "Error", Dialog.Message.Error)
  }

}