using Abp.Application.Services.Dto;
using Abp.AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Tags.Dto
{
    [AutoMapTo(typeof(Tag))]
    public class UpdateTagDto : EntityDto<long>
    {
        public string Name { get; set; }
        public string Address { get; set; }
        public string Color { get; set; }
    }
}
