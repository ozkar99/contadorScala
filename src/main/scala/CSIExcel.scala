import java.io.{FileOutputStream, File, FileInputStream}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.Sheet
import scala.annotation.tailrec
import org.apache.poi.ss.usermodel.Cell


class CSIExcel(val filePath: String) {

  private val fileIS = new FileInputStream(new File(filePath))

  private val wb = new XSSFWorkbook(fileIS)
  private val sheet0 = wb.getSheetAt(0)
  private val sheetList : List[Sheet] = createSheetList
  private val lmadList: Set[String] = createLmadList

  def process = {

    innerprocess(sheetList)
    wb.write(new FileOutputStream(new File(filePath)))

    @tailrec
    def innerprocess(l: List[Sheet]): Boolean = {
      if(l.isEmpty) /*break condition*/
        true
      else {
        /*Do sheet processing*/
        insertAlumniGrades(l.head, 10, 5)

        /*recursive call*/
        innerprocess(l.tail)
      }
    }
  }



  private def isLMAD(alumno: String): Boolean = containsName(alumno)
  private def containsName(alumno: String): Boolean = lmadList.contains(alumno)

  /*Crea nuestra lista de hojas para procesar*/
  private def createSheetList: List[Sheet] = {
    var retval : List[Sheet] = List()
    val sheetNo = wb.getNumberOfSheets
    for( i <- 1 until sheetNo) {
      retval = wb.getSheetAt(i) :: retval
    }
    retval
  }

  /*Set de matriculas de la pagina 1*/
  private def createLmadList: Set[String] = {
    var retval: Set[String] = Set()
    retval
  }

  /*Inserta cantidad de alumnos aprobados y inaprobados en la pagina*/
  private def insertAlumniGrades(sheet: Sheet, aprobados: Integer, noAprobados: Integer) = {
    val aprobadosS = "# Alumnos Aprobados: " + aprobados.toString
    val noAprobadosS = "# Alumnos NO Aprobados: " + noAprobados.toString

    /*
     * Escribir en la sheet especificada row 1 y 2, columna D
     */
    sheet.getRow(0).createCell(3).setCellValue(aprobadosS)
    sheet.getRow(1).createCell(3).setCellValue(noAprobadosS)

  }

}