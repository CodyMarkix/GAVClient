<page actionBarHidden="true">
    <gridLayout rows="100, 100, 100, 100, 100, 100, 100, 100" columns="100*, 100*, 100*">
        <label text="0,1" row="0" col="0" backgroundColor="#1c486b"/>
        <!--<image src="~/assets/logo.png" row="0" col="1" style="margin: 2%"/> -->
        <image src="~/assets/placeholderAccount.jpg" row="0" col="2" style="margin: 3%"/>
    </gridLayout>

</page>

<script lang="ts">
    import { onMount } from "svelte";
    let message: string = "Blank Svelte Native App";

    onMount(() => {
        console.log("Hello world!")
    });
</script>

<style>

</style>
