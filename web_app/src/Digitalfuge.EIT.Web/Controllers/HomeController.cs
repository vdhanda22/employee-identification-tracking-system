﻿using System.Web.Mvc;
using Abp.Web.Mvc.Authorization;

namespace Digitalfuge.EIT.Web.Controllers
{
    [AbpMvcAuthorize]
    public class HomeController : EITControllerBase
    {
        public ActionResult Index()
        {
            return View("~/App/Main/views/layout/layout.cshtml"); //Layout of the angular application.
        }
	}
}