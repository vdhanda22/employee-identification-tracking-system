using Abp.Application.Services;
using Abp.Application.Services.Dto;
using Abp.Domain.Repositories;
using Digitalfuge.EIT.Employees;
using Digitalfuge.EIT.Tags.Dto;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Tags
{
    public class TagAppService : AsyncCrudAppService<Tag, UpdateTagDto, long, PagedResultRequestDto, CreateTagDto, UpdateTagDto>, ITagAppService
    {
        private readonly IRepository<Employee, long> _employeeRepository;
        public TagAppService(
             IRepository<Tag, long> repository,
            IRepository<Employee, long> employeeRepository
        ) : base(repository)
        {
            _employeeRepository = employeeRepository;
        }

        public List<UpdateTagDto> GetUnusedTags()
        {
            var tagsList = _employeeRepository.GetAll()
            .Where(x => x.TagId.HasValue).Select(x => x.Tag.Id).ToList();
            var tags = Repository.GetAll()
                                 .Where(x => !tagsList.Contains(x.Id))
                                 .Select(x=> new UpdateTagDto()
                                 {
                                     Id=x.Id,
                                     Name=x.Name,
                                     Address=x.Address,
                                     Color=x.Color
                                 })
                                 .ToList();
            return tags;
        }
    }
}
