using Abp.Web.Models;
using Abp.WebApi.Controllers;
using Digitalfuge.EIT.Api.Models;
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
    public class LocationController : AbpApiController
    {
        private readonly LocationManager _locationManager;
        public LocationController(
            LocationManager locationManager
            )
        {
            _locationManager = locationManager;
        }

        [HttpPost]
        public async Task<AjaxResponse> StoreRfidData(RfidLocationModel rfidLocationModel)
        {
            try
            {
                await _locationManager.RecieveRfidData(rfidLocationModel.Data);
            }
            catch(Exception e)
            {
                throw e;
            }
            return new AjaxResponse("Success");
            
        }


    }
}
