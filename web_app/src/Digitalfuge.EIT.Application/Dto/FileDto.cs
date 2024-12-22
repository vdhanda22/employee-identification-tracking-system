using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Dto
{
    public class FileDto
    {
        public string FileName { get; set; }
        public string FileType { get; set; }
        public string FileToken { get; set; }

        public FileDto()
        {

        }

        public FileDto(string fileName, string fileType)
        {
            FileName = fileName;
            FileType = fileType;
            FileToken = Guid.NewGuid().ToString("N");
        }
    }
}
