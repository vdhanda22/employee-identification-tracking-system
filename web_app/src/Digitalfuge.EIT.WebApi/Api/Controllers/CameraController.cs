using Abp.Web.Models;
using Abp.WebApi.Controllers;
using Digitalfuge.EIT.Movements;
using Digitalfuge.EIT.Movements.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Http;

namespace Digitalfuge.EIT.Api.Controllers
{
    public class CameraController : AbpApiController
    {
        private readonly LocationManager _locationManager;
        public CameraController(
            LocationManager locationManager
            )
        {
            _locationManager = locationManager;
        }

        [HttpPost]
        public AjaxResponse StoreCctvData(CameraDataInputRequestDto cameraDataInputRequestDto)
        {
            try
            {
                _locationManager.RecieveCctvData(cameraDataInputRequestDto);
            }
            catch (Exception e)
            {
                throw e;
            }
            return new AjaxResponse("Success");

        }
    }
}
