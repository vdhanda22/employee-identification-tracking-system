using Abp.Domain.Entities;
using Digitalfuge.EIT.Employees;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements
{
    public class Movement:Entity<long>
    {
        public long EmployeeId { get; set; }
        public MovementType MovementType { get; set; }
        public DateTime ReportedOn { get; set; }
        public virtual Employee Employee { get; set; }
    }
}
