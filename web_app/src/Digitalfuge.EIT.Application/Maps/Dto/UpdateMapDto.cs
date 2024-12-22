using Abp.Application.Services.Dto;
using Abp.AutoMapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Maps.Dto
{
    [AutoMapTo(typeof(Map))]
    public class UpdateMapDto : EntityDto<long>
    {
        public string FileName { get; set; }
        public string Name { get; set; }
    }
}
