using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements.Dto
{
    public class GetLocationsResponseDto
    {
        public long TagId { get; set; }
        public ICollection<PositonsDto> Positons { get; set; }

    }
    public class PositonsDto
    {
        public long Id { get; set; }
        public string xpos { get; set; }
        public string ypos { get; set; }
    }
}
