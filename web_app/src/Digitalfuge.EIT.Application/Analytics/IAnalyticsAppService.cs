using Abp.Application.Services;
using Digitalfuge.EIT.Analytics.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Analytics
{
    public interface IAnalyticsAppService : IApplicationService
    {
        AttendanceGraphDto GetAttendanceGraph(GetAnalyticsInputDto request);
    }
}