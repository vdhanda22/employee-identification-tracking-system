using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Digitalfuge.EIT.Cameras.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Cameras
{
    public interface ICameraAppService : IAsyncCrudAppService<UpdateCameraDto, long, PagedResultRequestDto, CreateCameraDto, UpdateCameraDto>
    {
    }
}
