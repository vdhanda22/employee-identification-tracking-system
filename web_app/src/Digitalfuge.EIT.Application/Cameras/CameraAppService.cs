using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Abp.Domain.Repositories;
using Digitalfuge.EIT.Cameras.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Cameras
{
    public class CameraAppService : AsyncCrudAppService<Camera, UpdateCameraDto, long, PagedResultRequestDto, CreateCameraDto, UpdateCameraDto>, ICameraAppService
    {
        public CameraAppService(
            IRepository<Camera,long> repository) : base(repository)
        {

        }
    }
}
