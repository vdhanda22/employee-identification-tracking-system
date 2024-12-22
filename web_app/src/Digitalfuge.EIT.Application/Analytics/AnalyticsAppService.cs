using Abp.Domain.Repositories;
using Abp.Linq.Extensions;
using Digitalfuge.EIT.Analytics.Dto;
using Digitalfuge.EIT.Employees;
using Digitalfuge.EIT.Movements;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Core.Objects;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Analytics
{
    public class AnalyticsAppService : EITAppServiceBase, IAnalyticsAppService
    {
        private readonly IRepository<Location, long> _locationRepository;
        private readonly IRepository<Movement, long> _movementRepository;
        private readonly IRepository<Employee, long> _employeeRepository;
        public AnalyticsAppService(IRepository<Employee, long> employeeRepository, IRepository<Location, long> locationRepository, IRepository<Movement, long> movementRepository)
        {
            _locationRepository = locationRepository;
            _movementRepository = movementRepository;
            _employeeRepository = employeeRepository;
        }

        public AttendanceGraphDto GetAttendanceGraph(GetAnalyticsInputDto request)
        {
            var records = _movementRepository
                .GetAll()
                .WhereIf(request.FromDate.HasValue, e => e.ReportedOn >= request.FromDate.Value)
                .WhereIf(request.ToDate.HasValue, e => e.ReportedOn <= request.ToDate.Value)
                .Where(x => x.MovementType == MovementType.CHECK_IN)
                .GroupBy(x => DbFunctions.TruncateTime(x.ReportedOn).Value)
                .Select(x => new
                {
                    Date = x.Key,
                    Count = x.GroupBy(z => z.EmployeeId).Count()
                }).Select(x => new
                {
                    Date = x.Date,
                    Number = x.Count
                }).ToList();

            return new AttendanceGraphDto()
            {
                Date = records.Select(x => x.Date.ToString("dd/MMM/yy")).ToList(),
                Number = records.Select(x => x.Number).ToList()
            };
        }
    }
}
