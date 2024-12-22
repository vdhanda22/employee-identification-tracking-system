using Digitalfuge.EIT.Dto;
using Digitalfuge.EIT.Movements.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements
{
    public interface IMovementExcelExporter
    {
        Task<FileDto> ExportMovements(ExportMovementsInput input);
    }
}
