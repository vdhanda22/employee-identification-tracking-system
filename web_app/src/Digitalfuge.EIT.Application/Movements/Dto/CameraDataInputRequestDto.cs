using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements.Dto
{
    public class CameraDataInputRequestDto
    {
        public long CameraId { get; set; }
        public List<long> Employees { get; set; }
    }
}
