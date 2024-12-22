using Abp.AutoMapper;
using Digitalfuge.EIT.Employees.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements.Dto
{
    [AutoMapFrom(typeof(Movement))]
    public class MovementViewListDto
    {
        public long Id { get; set; }
        public long EmployeeId { get; set; }
        public DateTime ReportedOn { get; set; }
        public DateTime? FromDate { get; set; }
        public DateTime? ToDate { get; set; }

        public EmployeeListDto Employee { get; set; }
        public string MovementType { get; set; }
    }
}
