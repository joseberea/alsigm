// Descarga el Array del componente de las listas 
function OnUnloadListBusc()
{
   if (oListBusc)
   {
      oListBusc.DestructListView();
      oListBusc = null;
   }
   return;
}

// funciones de control del raton cuando se posiciona sobre las imagenes
function mouseOver()
{
	var element = top.GetSrcElement(event);	
   
	element.style.cursor = "pointer";
	window.status = "";
}
      
function mouseOut()
{
	var element = top.GetSrcElement(event);	
	
	element.style.cursor = "default";
	window.status = window.defaultStatus;
}

// Funcion que deshabilita los botones de la lista de busqueda
function DisabledButs()
{
	if (oListBusc.getNumRowsSelected() == 0){
		document.getElementById("btnAddInter").disabled = true;
	}	
	else {
		document.getElementById("btnAddInter").disabled = false;
	}
   
	return;
}

// funcion que habilita o deshabilita el boton de buscar
function PushUpInBuscar(aEvent)
{
	var element = top.GetSrcElement(aEvent);	
	
	switch (top.GetKeyCode(aEvent)) {
		case(13): 
			if (top.miTrim(top.Replace(element.value)) != "") {
				element.value = doBlur(element);
                BuscarInteresados();
             }
		default: 
			var oBtnBuscar = document.getElementById("btnBuscar");
			
			if (top.miTrim(top.Replace(element.value)) != ""){
                oBtnBuscar.onmouseover= mouseOver;
                oBtnBuscar.onmouseout= mouseOut;
                oBtnBuscar.onclick= BuscarInteresados;
                oBtnBuscar.src = "images/lupa.gif";
                oBtnBuscar.tabIndex = "2";
            }
            else {
				oBtnBuscar.onmouseover=null;
                oBtnBuscar.onmouseout=null;
                oBtnBuscar.onclick=null;
                oBtnBuscar.src = "images/lupadis.gif";
                oBtnBuscar.tabIndex = "-1";
            }
	}
   
	return;
}

// Detecta cambios en el campo
function OnChangeFind(oField)
{
	var oBtnBuscar = document.getElementById("btnBuscar");
	
	if (top.miTrim(oField.value) != "") {
		oBtnBuscar.onmouseover= mouseOver;
		oBtnBuscar.onmouseout= mouseOut;
		oBtnBuscar.onclick= BuscarInteresados;
		oBtnBuscar.src = "images/lupa.gif";
		oBtnBuscar.tabIndex = "2";
	}
	else  {
		oBtnBuscar.onmouseover=null;
		oBtnBuscar.onmouseout=null;
		oBtnBuscar.onclick=null;
		oBtnBuscar.src = "images/lupadis.gif";
		oBtnBuscar.tabIndex = "-1";
	}
}

// funcion que lanza el formulario de la busqueda
function BuscarInteresados()
{
	var oForm = document.getElementById("oFrmBuscInter");
	
	document.body.style.cursor = "wait";
	oForm.action = top.g_URL + "/vldbuscinter.jsp" + top.document.location.search;
	oForm.submit();
   
	return;
}


// Agrega un interesado a la lista de Interesados (la de arriba)
function AgregarInter()
{
   var oElem;
   var strText = ""
   for (var ii=0; ii < oListBusc.getListElems(); ii++)
   {
      if (oListBusc.oRows[ii].isSelected())
      {
         oElem = oListBusc.oRows[ii].oColumns[0]; 
         // El texto esta en la lista de la derecha
         strText = oListBusc.oRows[ii].oColumns[1].Text; 
         if (oElem.GetArgColumn("PersonType") == "1")
         {
            parent.VldInterList.AddElemtoLists(strText, oElem.Id,"",0, oElem.GetArgColumn("PersonType"), oElem.GetArgColumn("DocumentType"), oElem.GetArgColumn("Apellido1"), oElem.GetArgColumn("Apellido2"), oElem.GetArgColumn("PersonName"), 0);
         }
         else
         {
            parent.VldInterList.AddElemtoLists(strText, oElem.Id,"",0, oElem.GetArgColumn("PersonType"), oElem.GetArgColumn("DocumentType"), "", "", oElem.GetArgColumn("PersonName"), 0);
         }
         parent.VldInterList.DisabledControls();
         parent.g_VldInterSaved=true;
      }
   }
   return;
}



// pone el foco en el radio buton que teniamos antes
function SetRadioFocus()
{
	var index = !parent.iTipoBusc ? 0 : 1;
	
	document.getElementsByName("oRadioBus")[index].click();
   
	return;
}

// agrega una persona fisica a la lista
function AddInterFis(strIndex, strId, strDocType, strPerType,
      strPerName, strApe1, strApe2, strColText1)
{
	var iRet = 0;
	var iIndex = oListBusc.getListElems();
	var fullName = top.HTMLDecode(strApe1) + " " + top.HTMLDecode(strApe2) + ", " + top.HTMLDecode(strPerName);
   
	strColText1 = top.HTMLDecode(strColText1);
	
	// Agregar la fila
	iRet = oListBusc.AddRow(strId, strColText1);
   
	if (iRet != -1){
		// Agregamos la primera columna
		oListBusc.oRows[iIndex].AddColumn(strId, strColText1);
		// Agregamos los argumentos de la columna
		oListBusc.oRows[iIndex].oColumns[0].AddArgColumn("DocumentType", strDocType);
		oListBusc.oRows[iIndex].oColumns[0].AddArgColumn("PersonType", strPerType);
		oListBusc.oRows[iIndex].oColumns[0].AddArgColumn("PersonName", strPerName);
		oListBusc.oRows[iIndex].oColumns[0].AddArgColumn("Apellido1", strApe1);
		oListBusc.oRows[iIndex].oColumns[0].AddArgColumn("Apellido2", strApe2);
		// Agregamos la segunda columna
		oListBusc.oRows[iIndex].AddColumn(strId, fullName);
	}
   
	return; 
}

// agrega una persona juridica a la lista
function AddInterJur(strIndex, strId, strDocType, strPerType,
      strColText1, strColText2)
{
	var iRet = 0;
	var iIndex = oListBusc.getListElems();
   
	strColText1 = top.HTMLDecode(strColText1);
	// se sustituye en los campos TEXTAREA <BR> por \r\n
	strColText2 = strColText2.replace(/<BR>/g, "\r\n");
	strColText2 = top.HTMLDecode(strColText2);
   
	// Agregamos la fila
	iRet = oListBusc.AddRow(strId, strColText1);
   
	if (iRet != -1){
		// Agregamos la primera columna
		oListBusc.oRows[iIndex].AddColumn(strId, strColText1);
		// Agregamos los argumentos de la columna
		oListBusc.oRows[iIndex].oColumns[0].AddArgColumn("DocumentType", strDocType);
		oListBusc.oRows[iIndex].oColumns[0].AddArgColumn("PersonType", strPerType);
		// Agregamos la segunda columna
		oListBusc.oRows[iIndex].AddColumn(strId, strColText2);
	}
   
	return; 
}


function doBlur(obj)
{
	var desc = top.miTrim(obj.value);

	obj.value = top.Replace(desc);
}
