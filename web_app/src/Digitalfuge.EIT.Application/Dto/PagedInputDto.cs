using Abp;
using Abp.Application.Services.Dto;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Dto
{
    public class PagedInputDto : IPagedResultRequest
    {
        [Range(1, EITConsts.MaxPageSize)]
        public int MaxResultCount { get; set; }

        [Range(0, int.MaxValue)]
        public int SkipCount { get; set; }

        public PagedInputDto()
        {
            MaxResultCount = EITConsts.DefaultPageSize;
        }
    }
}
