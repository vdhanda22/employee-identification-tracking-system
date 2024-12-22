using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Digitalfuge.EIT.Movements.Dto
{
    public class TagCollection
    {
        public string tagAddr;
        public DateTime firstSeen;
        public List<TagReaderCollection> readers = new List<TagReaderCollection>();

    }
}
