using Abp.AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Maps.Dto
{
    [AutoMapTo(typeof(Map))]
    public class CreateMapDto
    {
        public string FileName { get; set; }
        public string Name { get; set; }
    }
}
