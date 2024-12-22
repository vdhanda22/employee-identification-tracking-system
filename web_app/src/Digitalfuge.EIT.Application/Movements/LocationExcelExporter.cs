using Digitalfuge.EIT.DataExporting.Excel.EpPlus;
using Digitalfuge.EIT.Dto;
using Digitalfuge.EIT.Movements.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements
{
    public class LocationExcelExporter : EpPlusExcelExporterBase, ILocationExcelExporter
    {
        public async Task<FileDto> ExportLocations(ExportLocationsInput input)
        {
            {
                return CreateExcelPackage(
                    "Locations.xlsx",
                    excelPackage =>
                    {
                        var sheet = excelPackage.Workbook.Worksheets.Add(L("Locations"));
                        sheet.OutLineApplyStyle = true;

                        var headers = new List<string>();
                        headers.Add(L("Id"));
                        headers.Add(L("Employee"));
                        headers.Add(L("Room"));
                        headers.Add(L("ReportedOn"));                        
                       
                        var fields = new List<Func<LocationViewListDto, object>>
                        {
                                  _ => _.Id,
                                  _ => _.Employee.Name,
                                  _ => _.Room.Name,
                                  _ => _.ReportedOn.ToString("dd/MM/yyyy HH:mm:ss")

                    };                       
                        AddHeader(
                         sheet,
                         headers.ToArray()
                   );


                        AddObjects(
                                sheet, 2, input.Locations,
                               fields.ToArray()
                                );

                        var invoiceDate = sheet.Column(3);
                        invoiceDate.Style.Numberformat.Format = "yyyy-mm-dd";                      
                        for (var i = 1; i <= headers.Count; i++)
                        {
                            sheet.Column(i).AutoFit();
                        }
                    });
            }
        }
    }
}
