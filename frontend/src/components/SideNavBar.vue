<template>
  <el-aside width="200px">
    <el-menu
      default-active="/"
      class="side-nav"
      mode="vertical"
      :default-openeds="category.map(item => item.id+'')"
    >
      <el-submenu
        v-for="a in category"
        :key="a.id"
        :index="a.id+''"
      >
        <template slot="title">{{ a.name }}</template>
        <el-menu-item
          v-for="b in a.children"
          :key="b.id"
          :index="b.id+''"
          @click="pushCategory(b)"
        >
          {{ b.name }}</el-menu-item>
      </el-submenu>
    </el-menu>
  </el-aside>
</template>

<script>
export default {
  data() {
    return {
      category: []
    }
  },
  methods: {
    filterCategory(res) {
      let childArray = []
      while (res.length > 0) {
        let r = res.pop()
        if (r.parentId == 0) {
          this.category.push({
            id: r.id,
            name: r.name,
            status: r.status
          })
        } else {
          childArray.push(r)
        }
      }
      this.category.map(item => {
        item.children = childArray.filter(function(child) {
          return child.parentId == item.id
        })
      })
    },
    getAllCategory() {
      this.$axios
        .get('/category')
        .then(res => {
          if (res.status == 200) {
            this.filterCategory(res.data.data)
          }
        })
        .catch(() => {})
    },
    pushCategory(category) {
      this.$router.push({ path: '/category', query: { id: category.id } })
    }
  },
  created: function() {
    this.getAllCategory()
  }
}
</script>

<style>
  .side-nav {
    width: 200px;
  }
</style>
