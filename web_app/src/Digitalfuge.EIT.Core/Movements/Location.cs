using Abp.Domain.Entities;
using Digitalfuge.EIT.Employees;
using Digitalfuge.EIT.Rooms;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements
{
    public class Location:Entity<long>
    {
        public long EmployeeId { get; set; }
        public long RoomId { get; set; }
        public DateTime ReportedOn { get; set; }

        public virtual Employee Employee { get; set; }
        public virtual Room Room { get; set; }

    }
}
