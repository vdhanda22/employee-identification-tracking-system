using Abp.Runtime.Validation;
using Digitalfuge.EIT.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements.Dto
{
    public class GetMovementInputDto : PagedSortedAndFilteredInputDto, IShouldNormalize
    {
        public long? EmployeeId { get; set; }
        public DateTime? FromDate { get; set; }
        public DateTime? ToDate { get; set; }
        public string MovementType { get; set; }

        public void Normalize()
        {
            if (string.IsNullOrEmpty(Sorting))
                Sorting = "employeeId";
        }
    }
}
