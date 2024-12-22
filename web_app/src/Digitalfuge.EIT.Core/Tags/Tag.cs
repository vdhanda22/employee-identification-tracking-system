using Abp.Domain.Entities;
using Digitalfuge.EIT.Employees;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Tags
{
    public class Tag:Entity<long>
    {
        public string Name { get; set; }
        public string Address { get; set; }
        public string Color { get; set; }
    }
}
