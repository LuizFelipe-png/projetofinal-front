
alert("Ola mundo!");

function validarCampos(){
    const email = document.getElementById("email");
    const senha = document.getElementById("senha");
    const btn = document.getElementById("btn-logar");
    
    if(email.target.value.lenght > 0 && senha.target.lenght > 0 ""){
       btn.disabled = false;
    }
}