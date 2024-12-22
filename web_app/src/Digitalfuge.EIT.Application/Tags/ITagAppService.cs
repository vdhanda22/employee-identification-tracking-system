using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.Tags.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Tags
{
    public interface ITagAppService : IAsyncCrudAppService<UpdateTagDto, long, PagedResultRequestDto, CreateTagDto, UpdateTagDto>
    {
        List<UpdateTagDto> GetUnusedTags();
    }
}
