         <!-- Nested node template -->
         <script type="text/ng-template" id="nodes_renderer.html">
            <div>
               <div class='condition-panel' ng-controller="Child" ng-init="user=node.user;data=node">
            	  <div class='remove-condition'>
            		 <a ng-click="remove($parent.data,$index)">
            		 	<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
            		 </a>
            	  </div>
            	  <select ng-model="data.property" ng-change="changedValue(node)" 
            		 class='property-select form-control' >
            		 <option value="off">Select a property</option>
            		 <option ng-repeat="item in properties" value={{item.contributors_url}}>{{item.name}}</option>
            	  </select>
            <!-- parent is used because of ng-if-->
            	  <select ng-model="$parent.data.selectedUser" ng-change="changedUserValue(node)" ng-if="data.property != 'off'">
            		 <option value="off">Select a property</option>
            		 <option ng-repeat="item in users" value={{item.login}}>{{item.login}}</option>
            	  </select>
            	  <ol style=" list-style-type: none;">
            		 <li ng-repeat="node in node.nodes"  ng-include="'nodes_renderer.html'" >
            		 </li>
            	  </ol>
            	  <div ng-if="node.nodes && node.nodes.length > 0" class='condition-operator'> <a ng-click="newSubItem(node,'')" href='#'><span class="glyphicon glyphicon-plus" aria-hidden="true"></span></a></div>
               
      </div>
      </div>
</script>