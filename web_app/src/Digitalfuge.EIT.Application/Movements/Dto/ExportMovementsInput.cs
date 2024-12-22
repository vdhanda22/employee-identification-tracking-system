using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements.Dto
{
    public class ExportMovementsInput
    {
        public List<MovementViewListDto> Movements { get; set; }
        //public double TimeZoneDifferenceInMinutes { get; set; }
        public string DateFormat { get; set; }
    }
}
