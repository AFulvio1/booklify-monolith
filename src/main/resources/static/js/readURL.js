function readURL(input){
    if(input.files && input.files[0]){
        const reader = new FileReader();
        reader.onload = function(e){
            $('#imgPreview').attr('src', e.target.result).width(100).height(100);
        }
        reader.readAsDataURL(input.files[0])
    }
}
$('#bookImage').change(function(){
    readURL(this);
});
$(".custom-file-input").on("change", function() {
    const fileName = $(this).val().split("\\").pop();
    $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});