<template>
    <div class="dropdown">
        <button class="btn btn-primary dropdown-toggle w-100" type="button" id="dropdownMenuButton1"
            data-bs-toggle="dropdown" aria-expanded="false">
            <font-awesome-icon icon="fa-solid fa-location-dot" class="pe-2" />
            <span v-if="selectedItem">{{ selectedItem }}</span>
            <span v-else>Standort auswählen</span>
        </button>
        <div class="dropdown-menu w-100 py-0" aria-labelledby="dropdownMenuButton1">
            <input v-model="search" class="form-control text-center p-0" type="text"
                placeholder="Landkreis oder Postleitzahl" />
            <div v-if="filteredItems.length < 1" class="smallText text-danger text-center p-2">
                Leider ist kein Standort mit diesen Merkmalen verfügbar.
            </div>
            <ul class="list-group scrollSearch">
                <li @click="selectItem(item)" v-for="item in filteredItems" :key="item"
                    class="list-group-item dropdown-item text-center smallText p-1">
                    {{ item }}
                </li>
            </ul>
        </div>
    </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from 'vue';
import type { PropType } from 'vue';
import { mapping_plz_landkreis } from '@/assets/plz/mapping_plz_landkreis';

export default defineComponent({
    name: 'LocationSelectSearch',
    emits: ["selected-location"],
    props: {
        locations: { type: Array as PropType<string[]>, required: true },
        selected: { type: String, required: true },
    },
    setup(props, context) {
        const search = ref("");
        const items = ref(props.locations);
        const selectedItem = ref(props.selected);
        const filteredItems = computed(() => {
            const n: number = search.value.length;
            if (n < 1) {
                return items.value;
            } else {
                if (isNaN(Number(search.value))) {
                    return items.value.filter(i => i.toLowerCase().includes(search.value.toLowerCase()));
                } else {
                    const filtered = [...mapping_plz_landkreis.entries()].filter(i => i[0].includes(search.value.toLowerCase()));
                    return [...new Set(filtered.map(lk => lk[1]))];
                }
            }
        });
        function selectItem(item: string) {
            selectedItem.value = item;
            search.value = "";
            context.emit("selected-location", item);
        }
        return {
            search,
            selectedItem,
            items,
            filteredItems,
            selectItem,
        }
    },
})
</script>

<style lang="scss">
.scrollSearch {
    max-height: 150px;
    overflow-y: auto;
}
</style>
