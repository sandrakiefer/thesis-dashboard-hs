<template>
    <Dashboard />
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import Dashboard from '@/components/dashboard/Dashboard.vue';

export default defineComponent({
    name: "DashboardView",
    components: {
        Dashboard
    }
})
</script>

<style lang="scss">
</style>
