using Abp.Runtime.Validation;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Employees.Dto
{
    public class CreateOrUpdateEmployeeInput: IShouldNormalize
    {
        public EditEmployeeDto Employee { get; set; }
        public void Normalize()
        {
            if (Employee.DateOfBirth == null)
                Employee.DateOfBirth = System.DateTime.Now;
        }
    }
}
