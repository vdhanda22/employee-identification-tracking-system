using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements.Dto
{
    public class ExportLocationsInput
    {
        public List<LocationViewListDto> Locations { get; set; }
        //public double TimeZoneDifferenceInMinutes { get; set; }
        public string DateFormat { get; set; }
    }
}
